# clean-jdbc.util

A zero-dependency JDBC connection-pooling wrapper layer, under `ru.myx.sql.wrapper`. Several pool-sized driver variants (`DriverPool2x2`, `DriverPool4x4`, `DriverPool8x4`, `DriverPool16x4`), plus `DriverSticky` (sticky/pinned connections), `DriverNoImplicitTransactions`, and `DriverAlternateBlobs` (alternate blob handling) — pick the driver variant matching the pooling/transaction behavior needed rather than configuring one driver generically.
