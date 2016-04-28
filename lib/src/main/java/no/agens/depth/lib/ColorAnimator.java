/**
*  Copyright 2013 Alex Curran
* 
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*
*/

package no.agens.depth.lib;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * Class which allows animation between two colors. Compatible with NineOldAndroids or the standard
 * Android animation APIs (make sure you change the imports when using NOA).
 * <p/>
 * When using with background colors for views, ensure you use the {@link ViewBackgroundWrapper} class.
 * {@link View} does not have a <code>getBackgroundColor()</code> method required by the {@link ObjectAnimator}
 * framework; the wrapper class solves this issue.
 */
public class ColorAnimator {

    public static ObjectAnimator ofColor(Object target, String propertyName, int from, int to) {
		return ObjectAnimator.ofObject(target, propertyName, new ColorEvaluator(), from, to);
	}

	public static ObjectAnimator ofColor(Object target, String propertyName, int to) {
		return ObjectAnimator.ofObject(target, propertyName, new ColorEvaluator(), to);
	}
	
	public static ObjectAnimator ofBackgroundColor(View target, int from, int to) {
		return ObjectAnimator.ofObject(new ViewBackgroundWrapper(target), "backgroundColor", new ColorEvaluator(), from, to);
	}
	
	public static ObjectAnimator ofBackgroundColor(View target, int to) {
		return ObjectAnimator.ofObject(new ViewBackgroundWrapper(target), "backgroundColor", new ColorEvaluator(), to);
	}

	private static class ColorEvaluator implements TypeEvaluator<Integer> {

		@Override
		public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
			int startA, startR, startG, startB;
			int aDelta = (int) ((Color.alpha(endValue) - (startA = Color.alpha(startValue))) * fraction);
			int rDelta = (int) ((Color.red(endValue) - (startR = Color.red(startValue))) * fraction);
			int gDelta = (int) ((Color.green(endValue) - (startG = Color.green(startValue))) * fraction);
			int bDelta = (int) ((Color.blue(endValue) - (startB = Color.blue(startValue))) * fraction);
			return Color.argb(startA + aDelta, startR + rDelta, startG + gDelta, startB + bDelta);
		}
	}

	/**
	 * Helper class which allows retrieval of a {@link View}'s background as a color.
	 */
	public static class ViewBackgroundWrapper {

		private View mView;

		public ViewBackgroundWrapper(View v) {
			mView = v;
		}

		public int getBackgroundColor() {
			try {
				return ((ColorDrawable) mView.getBackground()).getColor();
			} catch (ClassCastException cce) {
				// The background isn't a ColorDrawable (could be BitmapDrawable etc.) - throw a more descriptive error
				throw new IllegalStateException(
						String.format("Attempt to read View background color when background isn't a ColorDrawable (is %s instead)",
								mView.getBackground().getClass().getSimpleName()));
			}
		}

		public void setBackgroundColor(int color) {
			mView.setBackgroundColor(color);
		}

	}

}
