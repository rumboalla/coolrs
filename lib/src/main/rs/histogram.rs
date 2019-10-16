#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

uint32_t size;
int32_t histo[256];
float histof[256];

inline static float bound(float val) {
    return fmin(1.0f, fmax(0.0f, val));
}

void init() {
    for (int i = 0; i < 256; i++) {
        histo[i] = 0;
        histof[i] = 0.0f;
    }
}

uchar4 __attribute__((kernel)) createHistogram(uchar4 in, uint32_t x, uint32_t y) {
    float4 color = rsUnpackColor8888(in);
    float Y = 0.299f * color.r + 0.587f * color.g + 0.114f * color.b;
    float U = ((0.492f * (color.b - Y)) + 1) / 2;
    float V = ((0.877f * (color.r - Y)) + 1) / 2;
    rsAtomicInc(&histo[(int32_t)(Y * 255)]);
    return rsPackColorTo8888(Y, U, V, color.a);
}

void parseHistogram() {
    float sum = 0.0f;
    for (int i = 0; i < 256; i++) {
        sum += histo[i];
        histof[i] = sum / size;
    }
}

uchar4 __attribute__((kernel)) applyHistogram(uchar4 in, uint32_t x, uint32_t y) {
    float4 color = rsUnpackColor8888(in);
    float Y = histof[(int32_t)(color.r * 255)];
    float U = (2 * color.g) - 1;
    float V = (2 * color.b) - 1;
    return rsPackColorTo8888(bound(Y + 1.14f * V), bound(Y - 0.395f * U - 0.581f * V), bound(Y + 2.033f * U), color.a);
}
