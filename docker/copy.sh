#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20220814.sql ./mysql/db
cp ../sql/ry_config_20220510.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../cms-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy cms-gateway "
cp ../cms-gateway/target/cms-gateway.jar ./ruoyi/gateway/jar

echo "begin copy cms-auth "
cp ../cms-auth/target/cms-auth.jar ./ruoyi/auth/jar

echo "begin copy cms-visual "
cp ../cms-visual/cms-monitor/target/cms-visual-monitor.jar  ./ruoyi/visual/monitor/jar

echo "begin copy cms-modules-system "
cp ../cms-modules/cms-system/target/cms-modules-system.jar ./ruoyi/modules/system/jar

echo "begin copy cms-modules-file "
cp ../cms-modules/cms-file/target/cms-modules-file.jar ./ruoyi/modules/file/jar

echo "begin copy cms-modules-job "
cp ../cms-modules/cms-job/target/cms-modules-job.jar ./ruoyi/modules/job/jar

echo "begin copy cms-modules-gen "
cp ../cms-modules/cms-gen/target/cms-modules-gen.jar ./ruoyi/modules/gen/jar

