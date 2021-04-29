package com.example.maskenforcerpolicyapp;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

import java.util.concurrent.ExecutionException;

public class MATLABtoJava {
    static String msg;
    static int masks;
    static int total;
    static float percentage;

    public MATLABtoJava() throws InterruptedException, ExecutionException {
        MatlabEngine eng = MatlabEngine.startMatlab();
        masks = 0;
        eng.eval("AviFaceDetect.m");
        total = eng.getVariable("TotalPpl");
        percentage = (float)((float)(masks))/(float)(total);
        if (percentage > 1 || percentage < 0 || masks < 0 || total < 0 || (masks > total)){
            msg = "Error found in inputs.";
            System.exit(1);
        }
    }
}
