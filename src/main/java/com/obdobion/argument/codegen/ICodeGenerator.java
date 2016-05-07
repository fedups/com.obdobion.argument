package com.obdobion.argument.codegen;

import java.util.List;

import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
public interface ICodeGenerator
{
    List<GeneratedElement> generateElements (String elementNamePrefix);

    void setArguments (List<ICmdLineArg<?>> arguments);
}
