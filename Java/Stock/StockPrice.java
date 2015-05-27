import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class StockPrice {

    public static void main(final String[] args) throws IOException,
            ParseException {

        final Scanner sc = new Scanner(new File(args[0]));
        sc.useDelimiter("[\\s,]+");
        sc.nextLine();
        final ArrayList<Entry> stockList = new ArrayList<Entry>();
        while (sc.hasNext()){
            String date = sc.next();
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -100);
            final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
            df.set2DigitYearStart(cal.getTime());
            final Date d = df.parse(date);
            BigDecimal open = sc.nextBigDecimal();
            BigDecimal high = sc.nextBigDecimal();
            BigDecimal low = sc.nextBigDecimal();
            BigDecimal close = sc.nextBigDecimal();
            BigDecimal volume = sc.nextBigDecimal();
            BigDecimal adj = sc.nextBigDecimal();
            Entry element = new Entry (d, open, high, low, close, volume, adj);
            stockList.add(element);
            System.out.println(stockList.get(stockList.size()-1));
        }
        
    }

    static class Entry {
        public final Date d;
        public final BigDecimal open;
        public final BigDecimal high;
        public final BigDecimal low;
        public final BigDecimal close;
        public final BigDecimal volume;
        public final BigDecimal adj;
        
        public Entry (final Date d, final BigDecimal open, final BigDecimal high, final BigDecimal low,
                final BigDecimal close, final BigDecimal volume, final BigDecimal adj){
            this.d = d;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
            this.adj = adj;            
        }
        

    }

}
