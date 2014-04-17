#include <jni.h>
#include <stdio.h>
#include "opencv2/core/core.hpp"
#include <opencv2/imgproc/imgproc.hpp>
#include <vector>

using namespace std;
using namespace cv;

extern "C"
{
	JNIEXPORT jint JNICALL 
	Java_com_fenchtose_equalizehist_EqActivity_eqhist(
			JNIEnv* env, jobject,
			jint width, jint height, jintArray in, jintArray out)
	{
		jint* _in = env->GetIntArrayElements(in, 0);
		jint* _out = env->GetIntArrayElements(out, 0);

		Mat mSrc(height, width, CV_8UC4, (unsigned char*)_in);
		Mat bgra(height, width, CV_8UC4);

		vector<Mat> sChannels;
		split(mSrc, sChannels);

		for(int i=0; i<sChannels.size(); i++)
		{
			equalizeHist(sChannels[i], sChannels[i]);
		}

		merge(sChannels, bgra);

		for(int i=0; i<height; i++)
		{
			memcpy(&(_out[i*width]), &(bgra.data[i*bgra.step]), width*bgra.channels());
		}

		jint retVal;
		int ret = 1;
		retVal = jint(retVal);
		return retVal;
	}
}
