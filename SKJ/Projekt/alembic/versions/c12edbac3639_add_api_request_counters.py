"""add_api_request_counters

Revision ID: c12edbac3639
Revises: df38b7c80c69
Create Date: 2026-04-09 20:35:01.598940

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = 'c12edbac3639'
down_revision: Union[str, Sequence[str], None] = 'df38b7c80c69'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    """Upgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    if "buckets" not in inspector.get_table_names():
        return

    bucket_columns = {column["name"] for column in inspector.get_columns("buckets")}

    with op.batch_alter_table("buckets") as batch_op:
        if "count_write_requests" not in bucket_columns:
            batch_op.add_column(sa.Column("count_write_requests", sa.Integer(), nullable=False, server_default="0"))
        if "count_read_requests" not in bucket_columns:
            batch_op.add_column(sa.Column("count_read_requests", sa.Integer(), nullable=False, server_default="0"))


def downgrade() -> None:
    """Downgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    if "buckets" not in inspector.get_table_names():
        return

    bucket_columns = {column["name"] for column in inspector.get_columns("buckets")}

    with op.batch_alter_table("buckets") as batch_op:
        if "count_read_requests" in bucket_columns:
            batch_op.drop_column("count_read_requests")
        if "count_write_requests" in bucket_columns:
            batch_op.drop_column("count_write_requests")
