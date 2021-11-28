import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Invoice invoice = new Invoice();
        invoice.GAZTDefaultValues();

        System.out.println(invoice.GenerateHexString());
        System.out.println(invoice.GenerateBase64String());
    }
}