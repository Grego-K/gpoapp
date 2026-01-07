package gr.aueb.cf.gpoapp.core.exceptions;

public class AppServerException extends EntityGenericException {

  public AppServerException(String code, String message) {
    super(code, message);
  }
}