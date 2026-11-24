package io.github.ez500.CS180_GPJ.interfaces;

import java.io.IOException;

public interface Server {
    void initialize() throws IOException;
    void execute() throws IOException;
    void end(boolean interrupted) throws IOException;
    void cancel() throws IOException;
    boolean isFinished() throws IOException;
}
