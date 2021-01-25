package net.swifthq.swiftapi.text;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextUtils {
	public static Text createSystemText(String string) {
		return new LiteralText("[Server: " + string + "]").setStyle(new Style()
				.setColor(Formatting.GRAY)
				.setItalic(true));
	}
}
