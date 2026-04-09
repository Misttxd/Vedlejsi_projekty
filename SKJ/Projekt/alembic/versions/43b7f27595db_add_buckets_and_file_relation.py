"""add_buckets_and_file_relation

Revision ID: 43b7f27595db
Revises: 
Create Date: 2026-04-09 20:14:49.294225

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = '43b7f27595db'
down_revision: Union[str, Sequence[str], None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    """Upgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    tables = inspector.get_table_names()

    if "buckets" not in tables:
        op.create_table(
            "buckets",
            sa.Column("id", sa.Integer(), primary_key=True, autoincrement=True),
            sa.Column("name", sa.String(length=100), nullable=False, unique=True),
            sa.Column("created_at", sa.DateTime(), nullable=False),
        )

    if "files" not in tables:
        op.create_table(
            "files",
            sa.Column("id", sa.String(length=36), nullable=False),
            sa.Column("user_id", sa.String(length=64), nullable=False),
            sa.Column("bucket_id", sa.Integer(), nullable=True),
            sa.Column("filename", sa.String(length=255), nullable=False),
            sa.Column("path", sa.String(length=500), nullable=False),
            sa.Column("size", sa.Integer(), nullable=False),
            sa.Column("created_at", sa.DateTime(), nullable=False),
            sa.ForeignKeyConstraint(["bucket_id"], ["buckets.id"]),
            sa.PrimaryKeyConstraint("id"),
            sa.UniqueConstraint("path"),
        )
        op.create_index("ix_files_user_id", "files", ["user_id"], unique=False)
        op.create_index("ix_files_bucket_id", "files", ["bucket_id"], unique=False)
        return

    file_columns = {column["name"] for column in inspector.get_columns("files")}
    file_indexes = {index["name"] for index in inspector.get_indexes("files")}

    with op.batch_alter_table("files") as batch_op:
        if "bucket_id" not in file_columns:
            batch_op.add_column(sa.Column("bucket_id", sa.Integer(), nullable=True))
            batch_op.create_foreign_key("fk_files_bucket_id_buckets", "buckets", ["bucket_id"], ["id"])

        if "ix_files_bucket_id" not in file_indexes:
            batch_op.create_index("ix_files_bucket_id", ["bucket_id"], unique=False)


def downgrade() -> None:
    """Downgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    tables = inspector.get_table_names()

    if "files" in tables:
        file_columns = {column["name"] for column in inspector.get_columns("files")}
        file_indexes = {index["name"] for index in inspector.get_indexes("files")}

        if "bucket_id" in file_columns:
            with op.batch_alter_table("files") as batch_op:
                if "ix_files_bucket_id" in file_indexes:
                    batch_op.drop_index("ix_files_bucket_id")
                batch_op.drop_column("bucket_id")

    if "buckets" in tables:
        op.drop_table("buckets")
