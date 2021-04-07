# mosp

## MosP公式サイト

https://mosp.jp/

## OpenSource

https://ja.osdn.net/projects/mosp/

## Version

`4.6.3`

# Git管理

①プロジェクトフォルダを作成する

②ダウンロードした資材をプロジェクトフォルダに置く

③リモートリポジトリにPushする

`$ git add -A`

`$ git branch -M main`

`$ git remote add origin https://github.com/hikarizhen/mosp.git`

`$ git push -u origin main`

# 環境構築

## psotgresql install

```
$ brew search postgresql
==> Formulae
postgresql       postgresql@10    postgresql@11    postgresql@12    postgresql@9.4   postgresql@9.5   postgresql@9.6
==> Casks
homebrew/cask/navicat-for-postgresql
```

ガイドライン　

DB PostgreSQL 9.2.X

`$ brew install postgresql`

`$ postgres --version`

postgres (PostgreSQL) 13.2

## use postgres

# tomcat7
## install

取得

https://tomcat.apache.org/download-70.cgi


ローカルにダウンロードする。

インストール先フォルダで解凍する

`$ unzip apache-tomcat-7.0.108.zip`

`cd apache-tomcat-7.0.108/bin`

`$ chmod +x *.sh`

tomcatを起動する


```
$ ./startup.sh
Using CATALINA_BASE:   /Users/jinghuizhen/project/apache-tomcat-7.0.108
Using CATALINA_HOME:   /Users/jinghuizhen/project/apache-tomcat-7.0.108
Using CATALINA_TMPDIR: /Users/jinghuizhen/project/apache-tomcat-7.0.108/temp
Using JRE_HOME:        /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home
Using CLASSPATH:       /Users/jinghuizhen/project/apache-tomcat-7.0.108/bin/bootstrap.jar:/Users/jinghuizhen/project/apache-tomcat-7.0.108/bin/tomcat-juli.jar
Using CATALINA_OPTS:   
Tomcat started.
```
