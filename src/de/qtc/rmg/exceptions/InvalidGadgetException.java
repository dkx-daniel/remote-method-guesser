package de.qtc.rmg.exceptions;

/**
 * @author Tobias Neitzel (@qtc_de)
 */
public class InvalidGadgetException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidGadgetException() {}

    public InvalidGadgetException(String message)
    {
       super(message);
    }
}
