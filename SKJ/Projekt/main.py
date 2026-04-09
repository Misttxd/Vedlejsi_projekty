from datetime import datetime, timezone
import os
from typing import Annotated, Generator
import uuid

import aiofiles
from fastapi import Depends, FastAPI, File, Header, HTTPException, Path, Query, Request, UploadFile
from fastapi.responses import FileResponse
from pydantic import BaseModel, Field
from sqlalchemy import Boolean, DateTime, ForeignKey, Integer, String, create_engine, select
from sqlalchemy.exc import IntegrityError, SQLAlchemyError
from sqlalchemy.orm import DeclarativeBase, Mapped, Session, mapped_column, relationship, sessionmaker

app = FastAPI(title="Object Storage API")

STORAGE_DIR = "storage"
DATABASE_URL = "sqlite:///./storage.db"
USER_ID_PATTERN = r"^[A-Za-z0-9_-]+$"
UUID_PATTERN = r"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
BUCKET_NAME_PATTERN = r"^[A-Za-z0-9][A-Za-z0-9._-]{2,99}$"


class Base(DeclarativeBase):
    pass


class Bucket(Base):
    __tablename__ = "buckets"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String(100), unique=True)
    created_at: Mapped[datetime] = mapped_column(DateTime, default=lambda: datetime.now(timezone.utc))
    bandwidth_bytes: Mapped[int] = mapped_column(Integer, default=0)
    current_storage_bytes: Mapped[int] = mapped_column(Integer, default=0)
    ingress_bytes: Mapped[int] = mapped_column(Integer, default=0)
    egress_bytes: Mapped[int] = mapped_column(Integer, default=0)
    internal_transfer_bytes: Mapped[int] = mapped_column(Integer, default=0)
    count_write_requests: Mapped[int] = mapped_column(Integer, default=0)
    count_read_requests: Mapped[int] = mapped_column(Integer, default=0)

    objects: Mapped[list["StoredFile"]] = relationship(back_populates="bucket")


class StoredFile(Base):
    __tablename__ = "files"

    id: Mapped[str] = mapped_column(String(36), primary_key=True)
    user_id: Mapped[str] = mapped_column(String(64), index=True)
    bucket_id: Mapped[int | None] = mapped_column(ForeignKey("buckets.id"), index=True, nullable=True)
    filename: Mapped[str] = mapped_column(String(255))
    path: Mapped[str] = mapped_column(String(500), unique=True)
    size: Mapped[int] = mapped_column(Integer)
    created_at: Mapped[datetime] = mapped_column(DateTime, default=lambda: datetime.now(timezone.utc))
    is_deleted: Mapped[bool] = mapped_column(Boolean, default=False, index=True)

    bucket: Mapped[Bucket | None] = relationship(back_populates="objects")


class UserQuery(BaseModel):
    user_id: str = Field(
        ...,
        min_length=1,
        max_length=64,
        pattern=USER_ID_PATTERN,
        description="Identifikator uzivatele",
    )


class UploadQuery(BaseModel):
    user_id: str = Field(
        ...,
        min_length=1,
        max_length=64,
        pattern=USER_ID_PATTERN,
        description="Identifikator uzivatele",
    )
    bucket_id: int = Field(..., ge=1, description="ID bucketu")


class FileIdPath(BaseModel):
    id: str = Field(
        ...,
        pattern=UUID_PATTERN,
        description="Unikatni ID souboru",
    )


class BucketIdPath(BaseModel):
    bucket_id: int = Field(..., ge=1, description="ID bucketu")


class BucketCreateRequest(BaseModel):
    name: str = Field(
        ...,
        min_length=3,
        max_length=100,
        pattern=BUCKET_NAME_PATTERN,
        description="Unikatni nazev bucketu",
    )


class BucketResponse(BaseModel):
    id: int
    name: str
    created_at: datetime

    model_config = {"from_attributes": True}


class BucketBillingResponse(BaseModel):
    bucket_id: int
    bucket_name: str
    bandwidth_bytes: int = Field(..., ge=0)
    current_storage_bytes: int = Field(..., ge=0)
    ingress_bytes: int = Field(..., ge=0)
    egress_bytes: int = Field(..., ge=0)
    internal_transfer_bytes: int = Field(..., ge=0)
    count_write_requests: int = Field(..., ge=0)
    count_read_requests: int = Field(..., ge=0)


class FileMetadataResponse(BaseModel):
    id: str = Field(..., pattern=UUID_PATTERN)
    bucket_id: int | None = Field(default=None, ge=1)
    filename: str
    size: int = Field(..., ge=0)
    created_at: datetime

    model_config = {"from_attributes": True}


class DeleteFileResponse(BaseModel):
    message: str


engine = create_engine(
    DATABASE_URL,
    connect_args={"check_same_thread": False},
    echo=True,
)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False)


def get_db() -> Generator[Session, None, None]:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def get_current_user(
    user_id: Annotated[
        str,
        Query(
            ...,
            description="Identifikator uzivatele",
            min_length=1,
            max_length=64,
            pattern=USER_ID_PATTERN,
        ),
    ]
) -> UserQuery:
    return UserQuery(user_id=user_id)


def get_upload_query(
    user_id: Annotated[
        str,
        Query(
            ...,
            description="Identifikator uzivatele",
            min_length=1,
            max_length=64,
            pattern=USER_ID_PATTERN,
        ),
    ],
    bucket_id: Annotated[int, Query(..., ge=1, description="ID bucketu")],
) -> UploadQuery:
    return UploadQuery(user_id=user_id, bucket_id=bucket_id)


def get_file_id(
    id: Annotated[
        str,
        Path(
            ...,
            description="Unikatni ID souboru",
            pattern=UUID_PATTERN,
        ),
    ]
) -> FileIdPath:
    return FileIdPath(id=id)


def get_bucket_path_params(
    bucket_id: Annotated[int, Path(..., ge=1, description="ID bucketu")],
) -> BucketIdPath:
    return BucketIdPath(bucket_id=bucket_id)


def get_internal_source(
    x_internal_source: Annotated[bool, Header(alias="X-Internal-Source")] = False,
) -> bool:
    return x_internal_source


def _to_file_response(item: StoredFile) -> FileMetadataResponse:
    return FileMetadataResponse.model_validate(item)


def _get_bucket_or_404(db: Session, bucket_id: int) -> Bucket:
    bucket = db.get(Bucket, bucket_id)
    if bucket is None:
        raise HTTPException(status_code=404, detail="Bucket not found")
    return bucket


def _get_active_file_or_404(db: Session, file_id: str) -> StoredFile:
    stmt = select(StoredFile).where(
        StoredFile.id == file_id,
        StoredFile.is_deleted.is_(False),
    )
    file_info = db.scalar(stmt)
    if file_info is None:
        raise HTTPException(status_code=404, detail="File not found")
    return file_info


def _increment_bucket_transfer(bucket: Bucket, amount: int, internal_source: bool, mode: str) -> None:
    bucket.bandwidth_bytes += amount
    if mode == "upload":
        bucket.current_storage_bytes += amount
        if internal_source:
            bucket.internal_transfer_bytes += amount
        else:
            bucket.ingress_bytes += amount
        return

    if mode == "download":
        if internal_source:
            bucket.internal_transfer_bytes += amount
        else:
            bucket.egress_bytes += amount


def _resolve_bucket_id_for_request(request: Request, db: Session) -> int | None:
    path = request.url.path.rstrip("/")

    if path.startswith("/buckets/"):
        parts = path.split("/")
        if len(parts) >= 3 and parts[2].isdigit():
            return int(parts[2])
        return None

    if path == "/files/upload":
        bucket_id_raw = request.query_params.get("bucket_id")
        if bucket_id_raw and bucket_id_raw.isdigit():
            return int(bucket_id_raw)
        return None

    if path.startswith("/files/") or path.startswith("/objects/"):
        parts = path.split("/")
        if len(parts) >= 3:
            file_id = parts[2]
            file_info = db.scalar(select(StoredFile).where(StoredFile.id == file_id))
            if file_info is not None:
                return file_info.bucket_id

    return None


def _soft_delete_file(file_id: str, user: UserQuery, db: Session) -> DeleteFileResponse:
    file_info = _get_active_file_or_404(db, file_id)

    if file_info.user_id != user.user_id:
        raise HTTPException(status_code=403, detail="Access denied")

    file_info.is_deleted = True

    if file_info.bucket_id is not None:
        bucket = db.get(Bucket, file_info.bucket_id)
        if bucket is not None:
            bucket.current_storage_bytes = max(0, bucket.current_storage_bytes - file_info.size)

    try:
        db.commit()
    except SQLAlchemyError as exc:
        db.rollback()
        raise HTTPException(status_code=500, detail="Failed to soft-delete file") from exc

    return DeleteFileResponse(message="File marked as deleted")


os.makedirs(STORAGE_DIR, exist_ok=True)


@app.middleware("http")
async def count_bucket_api_requests(request: Request, call_next):
    method = request.method.upper()
    if method in {"GET", "POST", "PUT", "PATCH", "DELETE"}:
        with SessionLocal() as db:
            bucket_id = _resolve_bucket_id_for_request(request, db)
            if bucket_id is not None:
                bucket = db.get(Bucket, bucket_id)
                if bucket is not None:
                    if method == "GET":
                        bucket.count_read_requests += 1
                    else:
                        bucket.count_write_requests += 1
                    try:
                        db.commit()
                    except SQLAlchemyError:
                        db.rollback()

    response = await call_next(request)
    return response


@app.post("/buckets/", response_model=BucketResponse, tags=["buckets"])
async def create_bucket(payload: BucketCreateRequest, db: Session = Depends(get_db)):
    bucket = Bucket(name=payload.name)
    bucket.count_write_requests = 1
    try:
        db.add(bucket)
        db.commit()
        db.refresh(bucket)
    except IntegrityError as exc:
        db.rollback()
        raise HTTPException(status_code=409, detail="Bucket name already exists") from exc
    except SQLAlchemyError as exc:
        db.rollback()
        raise HTTPException(status_code=500, detail="Failed to create bucket") from exc

    return BucketResponse.model_validate(bucket)


@app.get("/buckets/{bucket_id}/objects/", response_model=list[FileMetadataResponse], tags=["buckets"])
async def list_bucket_objects(
    bucket_params: BucketIdPath = Depends(get_bucket_path_params),
    user: UserQuery = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    _get_bucket_or_404(db, bucket_params.bucket_id)
    stmt = (
        select(StoredFile)
        .where(
            StoredFile.bucket_id == bucket_params.bucket_id,
            StoredFile.user_id == user.user_id,
            StoredFile.is_deleted.is_(False),
        )
        .order_by(StoredFile.created_at.desc())
    )
    files = db.scalars(stmt).all()
    return [_to_file_response(file_item) for file_item in files]


@app.get("/buckets/{bucket_id}/billing/", response_model=BucketBillingResponse, tags=["buckets"])
async def get_bucket_billing(
    bucket_params: BucketIdPath = Depends(get_bucket_path_params),
    db: Session = Depends(get_db),
):
    bucket = _get_bucket_or_404(db, bucket_params.bucket_id)
    return BucketBillingResponse(
        bucket_id=bucket.id,
        bucket_name=bucket.name,
        bandwidth_bytes=bucket.bandwidth_bytes,
        current_storage_bytes=bucket.current_storage_bytes,
        ingress_bytes=bucket.ingress_bytes,
        egress_bytes=bucket.egress_bytes,
        internal_transfer_bytes=bucket.internal_transfer_bytes,
        count_write_requests=bucket.count_write_requests,
        count_read_requests=bucket.count_read_requests,
    )


@app.post("/files/upload", response_model=FileMetadataResponse, tags=["files"])
async def upload_file(
    file: UploadFile = File(...),
    upload_query: UploadQuery = Depends(get_upload_query),
    internal_source: bool = Depends(get_internal_source),
    db: Session = Depends(get_db),
):
    if not file.filename:
        raise HTTPException(status_code=400, detail="Filename is required")

    bucket = _get_bucket_or_404(db, upload_query.bucket_id)

    file_id = str(uuid.uuid4())
    user_dir = os.path.join(STORAGE_DIR, upload_query.user_id, str(bucket.id))
    os.makedirs(user_dir, exist_ok=True)
    file_path = os.path.join(user_dir, file_id)

    size = 0
    try:
        async with aiofiles.open(file_path, "wb") as buffer:
            while True:
                chunk = await file.read(1024 * 1024)
                if not chunk:
                    break
                size += len(chunk)
                await buffer.write(chunk)
    except OSError as exc:
        raise HTTPException(status_code=500, detail="Failed to store file on disk") from exc
    finally:
        await file.close()

    db_file = StoredFile(
        id=file_id,
        user_id=upload_query.user_id,
        bucket_id=bucket.id,
        filename=file.filename,
        path=file_path,
        size=size,
    )

    _increment_bucket_transfer(bucket, size, internal_source, mode="upload")

    try:
        db.add(db_file)
        db.commit()
        db.refresh(db_file)
    except SQLAlchemyError as exc:
        db.rollback()
        if os.path.exists(file_path):
            os.remove(file_path)
        raise HTTPException(status_code=500, detail="Failed to save file metadata") from exc

    return _to_file_response(db_file)


@app.get("/files/{id}", response_class=FileResponse, tags=["files"])
async def get_file(
    file_params: FileIdPath = Depends(get_file_id),
    user: UserQuery = Depends(get_current_user),
    internal_source: bool = Depends(get_internal_source),
    db: Session = Depends(get_db),
):
    file_info = _get_active_file_or_404(db, file_params.id)

    if file_info.user_id != user.user_id:
        raise HTTPException(status_code=403, detail="Access denied")

    if not os.path.exists(file_info.path):
        raise HTTPException(status_code=404, detail="File exists in DB but not on disk")

    if file_info.bucket_id is not None:
        bucket = db.get(Bucket, file_info.bucket_id)
        if bucket is not None:
            _increment_bucket_transfer(bucket, file_info.size, internal_source, mode="download")
            try:
                db.commit()
            except SQLAlchemyError as exc:
                db.rollback()
                raise HTTPException(status_code=500, detail="Failed to update bucket billing") from exc

    return FileResponse(file_info.path, filename=file_info.filename)


@app.delete("/files/{id}", response_model=DeleteFileResponse, tags=["files"])
async def delete_file(
    file_params: FileIdPath = Depends(get_file_id),
    user: UserQuery = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    return _soft_delete_file(file_params.id, user, db)


@app.delete("/objects/{object_id}", response_model=DeleteFileResponse, tags=["files"])
async def delete_object(
    object_id: Annotated[
        str,
        Path(..., pattern=UUID_PATTERN, description="Unikatni ID objektu"),
    ],
    user: UserQuery = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    return _soft_delete_file(object_id, user, db)


@app.get("/files", response_model=list[FileMetadataResponse], tags=["files"])
async def list_files(
    user: UserQuery = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    stmt = (
        select(StoredFile)
        .where(
            StoredFile.user_id == user.user_id,
            StoredFile.is_deleted.is_(False),
        )
        .order_by(StoredFile.created_at.desc())
    )
    files = db.scalars(stmt).all()
    return [_to_file_response(file_item) for file_item in files]

