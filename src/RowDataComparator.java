import java.io.Serializable;
import java.util.Comparator;

public class RowDataComparator implements Comparator<RowData>, Serializable{

    public int compare(RowData o1, RowData o2) {
        String value1 = o1.getValue();
        String value2 = o2.getValue();
        
        if (o1.isFirstRow() || o2.isFirstRow()) {
			return 1;
		}
        
//        if (value1.equals(value2)) {
//            return -1;
//        } else {
//            return 0;
//        }
        return value1.toLowerCase().compareTo(value2.toLowerCase());
    }

}