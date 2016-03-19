package com.obdobion.argument.codegen;

import java.util.List;

import com.obdobion.argument.ICmdLineArg;

/**
 * @author Chris DeGreef
 * 
 */
public interface ICodeGenerator
{
    void setArguments (List<ICmdLineArg<?>> arguments);

    List<GeneratedElement> generateElements (String elementNamePrefix);
}
