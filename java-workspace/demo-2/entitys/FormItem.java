package entitys;

public class FormItem {
    private String name;
    private Integer age;
    private String[] roles;
    private FormItem(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.roles = builder.roles.clone();
    }

    private static final class Builder {
        private String name;
        private Integer age;
        private String[] roles;

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder age(Integer age) {
            this.age = age;
            return this;
        }
        public Builder roles(String[] roles) {
            this.roles = roles.clone();
            return this;
        }

        public FormItem build() {
            return new FormItem(this);
        }
    }
}
