package net.scriptgate.softdev.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.opengl.GLES30.*;
import android.opengl.GLUtils;

public class TextureHelper
{
	public static int loadTexture(final Context context, final int resourceId)
	{
		final int[] textureHandle = new int[1];
		glGenTextures(1, textureHandle, 0);
		
		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
						
			// Bind to the texture in OpenGL
			glBindTexture(GL_TEXTURE_2D, textureHandle[0]);
			
			// Set filtering
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
			
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();						
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		return textureHandle[0];
	}
}
