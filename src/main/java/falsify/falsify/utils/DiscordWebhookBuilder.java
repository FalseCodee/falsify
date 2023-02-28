package falsify.falsify.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DiscordWebhookBuilder {
    private final JsonObject request;

    public DiscordWebhookBuilder() {
        this.request = new JsonObject();
    }

    public DiscordWebhookBuilder username(String name){
        request.addProperty("username", name);
        return this;
    }

    public DiscordWebhookBuilder avatar_url(String url){
        request.addProperty("avatar_url", url);
        return this;
    }

    public DiscordWebhookBuilder content(String content){
        request.addProperty("content", content);
        return this;
    }

    public EmbedBuilder embed() {
        if(!request.has("embeds")) request.add("embeds", new JsonArray());
        return new EmbedBuilder();
    }
    public class EmbedBuilder {
        private final JsonObject embed;

        public EmbedBuilder() {
            this.embed = new JsonObject();
        }

        public AuthorBuilder author() {
            return new AuthorBuilder();
        }

        public class AuthorBuilder {
            private final JsonObject author;

            public AuthorBuilder() {
                this.author = new JsonObject();
            }

            public AuthorBuilder name(String name) {
                author.addProperty("name", name);
                return this;
            }
            public AuthorBuilder website(String websiteUrl) {
                author.addProperty("url", websiteUrl);
                return this;
            }
            public AuthorBuilder icon(String imageUrl) {
                author.addProperty("icon_url", imageUrl);
                return this;
            }
            public EmbedBuilder build() {
                embed.add("author", author);
                return EmbedBuilder.this;
            }
        }

        public EmbedBuilder title(String title) {
            embed.addProperty("title", title);
            return this;
        }

        public EmbedBuilder url(String url) {
            embed.addProperty("url", url);
            return this;
        }

        public EmbedBuilder description(String description) {
            embed.addProperty("description", description);
            return this;
        }

        public EmbedBuilder color(int color) {
            embed.addProperty("color", color);
            return this;
        }

        public FieldBuilder field() {
            if(!embed.has("fields")) embed.add("fields", new JsonArray());
            return new FieldBuilder();
        }

        public class FieldBuilder {
            private final JsonObject field;

            public FieldBuilder() {
                this.field = new JsonObject();
            }

            public FieldBuilder name(String name) {
                field.addProperty("name", name);
                return this;
            }

            public FieldBuilder value(String value) {
                field.addProperty("value", value);
                return this;
            }

            public FieldBuilder inline(boolean inline) {
                field.addProperty("inline", inline);
                return this;
            }

            public EmbedBuilder build() {
                embed.getAsJsonArray("fields").add(field);
                return EmbedBuilder.this;
            }
        }

        public EmbedBuilder thumbnail(String url) {
            JsonObject obj = new JsonObject();
            obj.addProperty("url", url);
            embed.add("thumbnail", obj);
            return this;
        }

        public EmbedBuilder image(String url) {
            JsonObject obj = new JsonObject();
            obj.addProperty("url", url);
            embed.add("image", obj);
            return this;
        }

        public FooterBuilder footer() {
            return new FooterBuilder();
        }
        public class FooterBuilder {
            private final JsonObject footer;

            public FooterBuilder() {
                this.footer = new JsonObject();
            }

            public FooterBuilder text(String name) {
                footer.addProperty("text", name);
                return this;
            }
            public FooterBuilder icon(String url) {
                footer.addProperty("icon_url", url);
                return this;
            }

            public EmbedBuilder build() {
                embed.add("footer", footer);
                return EmbedBuilder.this;
            }
        }

        public DiscordWebhookBuilder build() {
            request.getAsJsonArray("embeds").add(embed);
            return DiscordWebhookBuilder.this;
        }

    }

    public JsonObject build() {
        return request;
    }

}
