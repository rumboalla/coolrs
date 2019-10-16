#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

uint32_t* palette;
uint8_t* buffer;
uint8_t* buffer2;
rs_allocation in;
uint32_t width;
uint32_t height;
float divider;

static inline uint32_t wrap(int32_t v, uint32_t m) { return v % m; }

static inline uint32_t wrapX(int32_t v) { return wrap(v, width); }

static inline uint32_t wrapY(int32_t v) { return wrap(v, height); }

void randomize() { for (int i = 0; i < width; i++) buffer[(height - 1) * width + i] = rsRand(255); }

void fire(uchar4* out, uint32_t x, uint32_t y) {

    uint32_t color = 0;

    color += buffer[wrapY(y + 1) * width + wrapX(x - 1)];
    color += buffer[wrapY(y + 1) * width + wrapX(x + 1)];
    color += buffer[wrapY(y + 1) * width + x];
    color += buffer[wrapY(y + 2) * width + x];
    color /= divider;

    uchar4 final;
    final.r = ((uchar *)&palette[color])[0];
    final.g = ((uchar *)&palette[color])[1];
    final.b = ((uchar *)&palette[color])[2];
    final.a = ((uchar *)&palette[color])[3];

    buffer2[y * width + x] = color;
    *out = final;
}

