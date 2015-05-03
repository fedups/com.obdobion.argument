package com.obdobion.argument.codegen;

/**
 * @author Chris DeGreef
 * 
 */
public class CodeGeneratorFactory
{
    static public ICodeGenerator create (CodeGeneratorType type)
    {
        switch (type)
        {
            case Java15String:
                return new Java15StaticStringGenerator();
            case Java15HardWire:
                return new Java15HardWireGenerator();
            default:
                return new Java15HardWireGenerator();
        }
    }
}
