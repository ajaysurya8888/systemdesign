package factory;

public class ExporterFactory {

    public static OrderExporter create(String format) {
        switch (format.toUpperCase()) {
            case "CSV":   return new CSVExporter();
            case "PDF":   return new PDFExporter();
            case "EXCEL": return new ExcelExporter();
            default:
                throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }
}