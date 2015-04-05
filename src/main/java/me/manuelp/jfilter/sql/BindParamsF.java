package me.manuelp.jfilter.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;

import java.sql.PreparedStatement;

public interface BindParamsF
    extends
    Callable1<Pair<ParamIndex, PreparedStatement>, Pair<ParamIndex, PreparedStatement>> {
}
