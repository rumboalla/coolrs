#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

float weight;
uint32_t width;
uint32_t height;
rs_allocation b1;
rs_allocation b2;

void blendTransition(uchar4* out, uint32_t x, uint32_t y) {
    // Get weighted color from both bitmaps and average them
    float4 color = rsUnpackColor8888(rsGetElementAt_uchar4(b1, x, y)) * weight;
    color += rsUnpackColor8888(rsGetElementAt_uchar4(b2, x, y)) * (1.0f - weight);

    // Force no alpha
    color.a = 1.0f;

    // Write pixel
    *out = rsPackColorTo8888(color);
}

void horizontalTransition(uchar4* out, uint32_t x, uint32_t y) {
    uint32_t l = width * weight;
    float4 color = 0.0f;

    if (x > l) {
        color = rsUnpackColor8888(rsGetElementAt_uchar4(b1, x, y));
    } else {
        color = rsUnpackColor8888(rsGetElementAt_uchar4(b2, x, y));
    }

    // Force no alpha
    color.a = 1.0f;

    // Write pixel
    *out = rsPackColorTo8888(color);
}

void verticalTransition(uchar4* out, uint32_t x, uint32_t y) {
    uint32_t l = height * weight;
    float4 color = 0.0f;

    if (y > l) {
        color = rsUnpackColor8888(rsGetElementAt_uchar4(b1, x, y));
    } else {
        color = rsUnpackColor8888(rsGetElementAt_uchar4(b2, x, y));
    }

    // Force no alpha
    color.a = 1.0f;

    // Write pixel
    *out = rsPackColorTo8888(color);
}
