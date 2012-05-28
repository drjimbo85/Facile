package com.drjimbo.facile;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetPhotoBitmap {
	
	


	public static Bitmap getPhotoBitmap(String url) {

		Bitmap uPhotoBitmap = null;
		try {
					URL jpegUrl = new URL(url);
					URLConnection conn = jpegUrl.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream input = conn.getInputStream();
					BufferedInputStream bInput = new BufferedInputStream(input);
					uPhotoBitmap = BitmapFactory.decodeStream(new FlushedInputStream(input));
					bInput.close();
					input.close();
			} catch (Exception e) {

					e.printStackTrace();
					
				}
		
		return uPhotoBitmap;

			}


	}
	
    class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }


