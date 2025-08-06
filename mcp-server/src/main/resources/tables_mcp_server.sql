#mcp-server 需要的表结构
# MCP-SERVER v1.1.0
# Copyright (c) 2025-present.

#创建数据库
create database aes_db

#创建scheme
CREATE SCHEMA aes_sc AUTHORIZATION aes_user;

# mcp服务信息表
CREATE TABLE aes_sc.mcp_server_info (
	seq_id numeric(24) NOT NULL,
	server_name varchar(50) NULL,
	server_version varchar(10) NULL,
	server_img text NULL,
	server_class varchar(255) NULL,
	server_status varchar(10) NULL,
	server_intro varchar(1000) NULL,
	main_feature varchar(2000) NULL,
	use_case varchar(2000) NULL,
	common_question varchar(2000) NULL,
	server_json varchar(2000) NULL,
	user_id numeric(24) NULL,
	create_time timestamp NULL,
	newserverid numeric(24) NULL,
	service_uuid varchar(255) NULL,
	area varchar(50) NULL,
	open_range varchar(50) NULL,
	access_type varchar(10) NULL,
	readme text NULL,
	CONSTRAINT pk_mcp_server_info PRIMARY KEY (seq_id)
);

# maas能力dcoos接口API
CREATE TABLE aes_sc.server_api_info (
	api_id varchar(255) NOT NULL,
	api_name varchar(255) NULL,
	api_type varchar(50) NULL,
	eop_protocol varchar(100) NULL,
	service_protocol varchar(50) NULL,
	eop_access_address varchar(500) NULL,
	api_describe varchar(500) NULL,
	auth_policy varchar(100) NULL,
	timeout_duration numeric(10) NULL,
	open_range varchar(100) NULL,
	is_public_network varchar(5) NULL,
	"system" varchar(255) NULL,
	area varchar(100) NULL,
	area_code varchar(100) NULL,
	large_scene varchar(255) NULL,
	small_scene varchar(255) NULL,
	api_state varchar(6) NULL,
	creator_id numeric(24) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	cn_api_name varchar(255) NULL,
	CONSTRAINT pk_server_api_info PRIMARY KEY (api_id)
);

# maas接口配置表
CREATE TABLE aes_sc.server_api_config (
	seq_id numeric(24) NOT NULL,
	api_id varchar(255) NULL,
	signed_system varchar(255) NULL,
	param_type numeric(10) NULL,
	param_name varchar(255) NULL,
	param_value text NULL,
	data_type varchar(100) NULL,
	param_position varchar(500) NULL,
	is_required numeric(10) NULL,
	description varchar(2000) NULL,
	parent_id numeric(24) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	example_value varchar(2000) NULL,
	supply_notes varchar(1000) NULL,
	access_way varchar(10) NULL,
	CONSTRAINT pk_server_api_config PRIMARY KEY (seq_id)
);

# mcp与api的关联表
CREATE TABLE aes_sc.wsc_mcp_api (
	seq_id numeric(24) NOT NULL,
	mcp_id numeric(24) NULL,
	api_id varchar(255) NULL,
	create_time timestamp NULL,
	CONSTRAINT wsc_mcp_api_pk PRIMARY KEY (seq_id)
);