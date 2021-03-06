package com.tongbanjie.baymax.jdbc.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 保存要执行的statement方法名称,用于在多个statement实例上循环执行.
 * @author dawei
 *
 */
public enum ExecuteMethod {
	/*----------------------Statement方法----------------------*/
	execute_sql,
	execute_sql_autoGeneratedKeys,
	execute_sql_columnIndexes,
	execute_sql_columnNames,
	
	executeQuery_sql,
	
	executeUpdate_sql,
	executeUpdate_sql_autoGeneratedKeys,
	executeUpdate_sql_columnIndexes,
	executeUpdate_sql_columnNames,
	
	/*-----------------TPreparedStatement方法------------------*/
	execute;
	
	public static enum MethodReturnType{
		int_type,boolean_type,result_set_type;
	}
	
	public MethodReturnType getReturnType(){
		switch (this) {
        case execute_sql:
        case execute_sql_autoGeneratedKeys:
        case execute_sql_columnIndexes:
        case execute_sql_columnNames:
        case execute:
        	return MethodReturnType.boolean_type;
        case executeQuery_sql:
        	return MethodReturnType.result_set_type;
        case executeUpdate_sql:
        case executeUpdate_sql_autoGeneratedKeys:
        case executeUpdate_sql_columnIndexes:
        case executeUpdate_sql_columnNames:
        	return MethodReturnType.int_type;
        default:
        	throw new IllegalArgumentException("Unhandled ExecuteMethod ReturnType:" + this.name());
		}
	}
	
    public Object executeMethod(Statement stmt, Object... args) throws SQLException {
        switch (this) {
            case execute_sql:
            	return stmt.execute((String) args[0]);
            case execute_sql_autoGeneratedKeys:
            	return stmt.execute((String)args[0], (Integer)args[1]);
            case execute_sql_columnIndexes:
            	return stmt.execute((String)args[0], (int[]) args[1]);
            case execute_sql_columnNames:
            	return stmt.execute((String)args[0], (String[])args[1]);
            	
            case executeQuery_sql:
            	return stmt.executeQuery((String)args[0]);
            
            case executeUpdate_sql:
            	return stmt.executeUpdate((String)args[0]);
            case executeUpdate_sql_autoGeneratedKeys:
            	return stmt.executeUpdate((String)args[0], (Integer)args[1]);
            case executeUpdate_sql_columnIndexes:
            	return stmt.execute((String)args[0], (int[])args[1]);
            case executeUpdate_sql_columnNames:
            	return stmt.execute((String)args[0], (String[])args[1]);
            	
            case execute:
            	return ((PreparedStatement)stmt).execute();
            default:
                throw new IllegalArgumentException("Unhandled ExecuteMethod:" + this.name());
        }
    }
    
    /**
     * StatementCreateMethod.autoGeneratedKeys() || ExecuteMethod.autoGeneratedKeys() == true表示需要返回自增键
     * 制作以要判断两个地方是应为PreparedStatement在创建时就能指定autoGeneratedKeys而不需要像Statement一样在execute方法中指定
     * @return
     */
    public boolean autoGeneratedKeys(){
		switch(this){
		// statement
		case execute_sql_autoGeneratedKeys:
		case execute_sql_columnIndexes:
		case execute_sql_columnNames:
		// preparedStatement
		case executeUpdate_sql_autoGeneratedKeys:
		case executeUpdate_sql_columnIndexes:
		case executeUpdate_sql_columnNames:
			return true;
		default:
			return false;
		}
	}
}
