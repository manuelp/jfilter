package me.manuelp.jfilter.sql;

import fj.P2;
import fj.function.Try1;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BindParamsF
    extends
    Try1<P2<ParamIndex, PreparedStatement>, P2<ParamIndex, PreparedStatement>, SQLException> {
}
