package libs;

/**
 * Exception lancée à l'échec du parsing d'un plan ou d'une demande de livraison XML
 */
public class ParseXmlException extends RuntimeException {
    public ParseXmlException(String exceptionChaine){
        super(exceptionChaine);
    }
}
