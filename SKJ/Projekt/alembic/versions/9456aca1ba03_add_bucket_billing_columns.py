"""add_bucket_billing_columns

Revision ID: 9456aca1ba03
Revises: 43b7f27595db
Create Date: 2026-04-09 20:14:57.432586

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = '9456aca1ba03'
down_revision: Union[str, Sequence[str], None] = '43b7f27595db'
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
        if "bandwidth_bytes" not in bucket_columns:
            batch_op.add_column(sa.Column("bandwidth_bytes", sa.Integer(), nullable=False, server_default="0"))
        if "current_storage_bytes" not in bucket_columns:
            batch_op.add_column(sa.Column("current_storage_bytes", sa.Integer(), nullable=False, server_default="0"))
        if "ingress_bytes" not in bucket_columns:
            batch_op.add_column(sa.Column("ingress_bytes", sa.Integer(), nullable=False, server_default="0"))
        if "egress_bytes" not in bucket_columns:
            batch_op.add_column(sa.Column("egress_bytes", sa.Integer(), nullable=False, server_default="0"))
        if "internal_transfer_bytes" not in bucket_columns:
            batch_op.add_column(sa.Column("internal_transfer_bytes", sa.Integer(), nullable=False, server_default="0"))


def downgrade() -> None:
    """Downgrade schema."""
    bind = op.get_bind()
    inspector = sa.inspect(bind)
    if "buckets" not in inspector.get_table_names():
        return

    bucket_columns = {column["name"] for column in inspector.get_columns("buckets")}

    with op.batch_alter_table("buckets") as batch_op:
        if "internal_transfer_bytes" in bucket_columns:
            batch_op.drop_column("internal_transfer_bytes")
        if "egress_bytes" in bucket_columns:
            batch_op.drop_column("egress_bytes")
        if "ingress_bytes" in bucket_columns:
            batch_op.drop_column("ingress_bytes")
        if "current_storage_bytes" in bucket_columns:
            batch_op.drop_column("current_storage_bytes")
        if "bandwidth_bytes" in bucket_columns:
            batch_op.drop_column("bandwidth_bytes")
