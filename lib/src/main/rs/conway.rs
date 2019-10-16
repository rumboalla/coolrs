#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

rs_allocation in;
uint32_t width;
uint32_t height;

static inline uint32_t wrap(int32_t v, uint32_t m) {
     if (v == -1) v = m - 1;
     if (v == m) v = 0;
     return v;
 }

static inline uint32_t wrapX(int32_t v) { return wrap(v, width);}

static inline uint32_t wrapY(int32_t v) { return wrap(v, height); }

static uint8_t checkNeighbour(uchar4 n) {
    if (n.r != 0 || n.g != 0 || n.b != 0) {
        return 1;
    } else {
        return 0;
    }
}

void conway(uchar4* out, uint32_t x, uint32_t y) {
    // Count neighbours
    uint8_t neighbour = checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x - 1), wrapY(y - 1)));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, x, wrapY(y - 1)));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x + 1), wrapY(y - 1)));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x - 1), y));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x + 1), y));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x - 1), wrapY(y + 1)));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, x, wrapY(y + 1)));
    neighbour += checkNeighbour(rsGetElementAt_uchar4(in, wrapX(x + 1), wrapY(y + 1)));

    // Apply rules
    if (checkNeighbour(rsGetElementAt_uchar4(in, x, y)) == 1) {
        if (neighbour < 2 || neighbour > 3) {
            *out = rsPackColorTo8888(0, 0, 0, 1.0f);
        } else {
            *out = rsPackColorTo8888(1.0f, 0, 0, 1.0f);
        }
    } else {
        if (neighbour == 3) {
            *out = rsPackColorTo8888(1.0f, 0, 0, 1.0f);
        } else {
            *out = rsPackColorTo8888(0, 0, 0, 1.0f);
        }
    }

}

