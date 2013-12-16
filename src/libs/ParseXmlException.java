package libs;

/**
 * Created by nightwish on 15/12/13.
 */
public class ParseXmlException extends RuntimeException {
    public ParseXmlException(){
        super();
    }
    public ParseXmlException(String exceptionChaine){
        super(exceptionChaine);
    }
    public ParseXmlException(String exceptionChaine, Throwable t){
        super(exceptionChaine,t);
    }
    public ParseXmlException(Throwable t){
        super(t);
    }

}
