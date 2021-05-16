package org.kodejava.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doPrint(View view) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    final String text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,
                            this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo,
                                    DisplayMetrics.DENSITY_LOW, getTheme())) + "</img>\n" +
                            "[L]\n" +
                            "[L]" + df.format(new Date()) + "\n" +
                            "[C]================================\n" +
                            "[L]<b>Effective Java</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(25000) + "\n" +
                            "[L]<b>Headfirst Android Development</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(45000) + "\n" +
                            "[L]<b>The Martian</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(20000) + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]TOTAL[R]" + nf.format(90000) + "\n" +
                            "[L]DISCOUNT 15%[R]" + nf.format(13500) + "\n" +
                            "[L]TAX 10%[R]" + nf.format(7650) + "\n" +
                            "[L]<b>GRAND TOTAL[R]" + nf.format(84150) + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
                            "[C]--------------------------------\n" +
                            "[C]Thanks For Shopping\n" +
                            "[C]https://kodejava.org\n" +
                            "[L]\n" +
                            "[L]<qrcode>https://kodejava.org</qrcode>\n";

                    printer.printFormattedText(text);
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }
}
