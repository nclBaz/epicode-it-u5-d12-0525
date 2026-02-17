package riccardogulin.u5d12.payloads;

public record NewBlogDTO(String title,
                         String content,
                         String category
                         // UUID authorId // <-- NON VA BENE PERCHE' NON POSSO CREARE POST CON UN AUTHORID DI ALTRI
) {
}
