package app.export;

import app.exception.DomainException;
import app.model.entity.Order;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static app.util.ExceptionMessages.ERROR_GENERATING_PDF;

public class ExporterPDF {

    public static byte[] exportOrders(List<Order> orders) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("My Orders Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{2.5f, 2.5f, 2.0f, 2.0f});

            addTableHeader(table);
            addTableData(table, orders);

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception ex) {
            throw new DomainException(ERROR_GENERATING_PDF);
        }
    }

    private static void addTableHeader(PdfPTable table) {

        Stream.of("Order ID", "Date", "Total (€)", "Status")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(Color.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTitle, new Font(Font.HELVETICA, 12, Font.BOLD)));
                    table.addCell(header);
                });
    }

    private static void addTableData(PdfPTable table, List<Order> orders) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy - HH:mm");
        for (Order o : orders) {
            table.addCell(o.getId().toString());
            table.addCell(o.getCreatedOn().format(formatter));
            table.addCell(formatPrice(o.getTotalAmount()));
            table.addCell(o.getStatus().name());
        }
    }

    private static String formatPrice(BigDecimal amount) {

        return amount != null
                ? amount.setScale(2, RoundingMode.HALF_UP).toString()
                : "-";
    }
}
