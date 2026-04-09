"""add_soft_delete_to_files

Revision ID: df38b7c80c69
Revises: 9456aca1ba03
Create Date: 2026-04-09 20:15:20.928265

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'df38b7c80c69'
down_revision: Union[str, Sequence[str], None] = '9456aca1ba03'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    """Upgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    if "files" not in inspector.get_table_names():
        return

    file_columns = {column["name"] for column in inspector.get_columns("files")}
    file_indexes = {index["name"] for index in inspector.get_indexes("files")}

    with op.batch_alter_table("files") as batch_op:
        if "is_deleted" not in file_columns:
            batch_op.add_column(sa.Column("is_deleted", sa.Boolean(), nullable=False, server_default=sa.text("0")))
        if "ix_files_is_deleted" not in file_indexes:
            batch_op.create_index("ix_files_is_deleted", ["is_deleted"], unique=False)


def downgrade() -> None:
    """Downgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    if "files" not in inspector.get_table_names():
        return

    file_columns = {column["name"] for column in inspector.get_columns("files")}
    file_indexes = {index["name"] for index in inspector.get_indexes("files")}

    with op.batch_alter_table("files") as batch_op:
        if "ix_files_is_deleted" in file_indexes:
            batch_op.drop_index("ix_files_is_deleted")
        if "is_deleted" in file_columns:
            batch_op.drop_column("is_deleted")
