<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqlMapConfig      
    PUBLIC "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN"      
    "http://ibatis.apache.org/dtd/sql-map-config-2.dtd">

<sqlMapConfig>
	<settings useStatementNamespaces="true" />
	<#list clazzinfos as clazzinfo>
		<sqlMap resource="${clazzinfo.packageName?replace(".","/")}/ibatis/${clazzinfo.tablename}_SqlMap.xml" />
	</#list>
</sqlMapConfig>