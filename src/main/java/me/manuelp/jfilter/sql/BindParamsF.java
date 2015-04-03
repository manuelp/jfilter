package me.manuelp.jfilter.sql;

import fj.function.Try1;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BindParamsF extends Try1<PreparedStatement, PreparedStatement, SQLException> {
}
