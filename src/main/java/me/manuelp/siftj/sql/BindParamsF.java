package me.manuelp.siftj.sql;

import fj.P2;
import fj.function.Try1;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Binder function that tries to bind parameters in a given
 * {@link PreparedStatement}, starting from the given {@link ParamIndex}.
 *
 * @see SqlFilter
 * @see WhereClause
 */
public interface BindParamsF extends
    Try1<P2<ParamIndex, PreparedStatement>, P2<ParamIndex, PreparedStatement>, SQLException> {
}
