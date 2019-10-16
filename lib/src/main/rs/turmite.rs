#pragma version(1)
#pragma rs java_package_name(com.coolrs.lib)
#pragma rs_fp_relaxed

rs_allocation in;
rs_allocation head;
uint32_t width;
uint32_t height;
uint8_t* turmite_def;
uint8_t turmite_num_colors;

static inline uint32_t wrap(int32_t v, uint32_t m) {
    if (v == -1) v = m -1;
    if (v == m) v = 0;
    return v;
}

static inline uint32_t wrap_x(int32_t v) { return wrap(v, width); }

static inline uint32_t wrap_y(int32_t v) { return wrap(v, height); }

static inline uint8_t get_color_id(uchar4 n) {
    if (n.r == 0 && n.g == 0 & n.b == 0) {
        return 0;
    } else if (n.r == 255 && n.g == 0 & n.b == 0) {
        return 1;
    } else if (n.r == 0 && n.g == 255 & n.b == 0) {
        return 2;
    } else if (n.r == 0 && n.g == 0 & n.b == 255) {
        return 3;
    } else if (n.r == 255 && n.g == 255 & n.b == 0) {
        return 4;
    } else if (n.r == 255 && n.g == 0 & n.b == 255) {
        return 5;
    } else if (n.r == 0 && n.g == 255 & n.b == 255) {
        return 6;
    } else if (n.r == 255 && n.g == 255 & n.b == 255) {
        return 7;
    } else if (n.r == 127 && n.g == 127 & n.b == 127) {
        return 8;
    } else if (n.r == 64 && n.g == 192 & n.b == 64) {
        return 9;
    }
    return 0;
}

static inline uchar4 get_color(uint8_t n) {
    uchar4 o;
    if (n == 0) {
        o.r = 0; o.g = 0; o.b = 0; o.a = 255;
    } else if (n == 1) {
        o.r = 255; o.g = 0; o.b = 0; o.a = 255;
    } else if (n == 2) {
        o.r = 0; o.g = 255; o.b = 0; o.a = 255;
    } else if (n == 3) {
        o.r = 0; o.g = 0; o.b = 255; o.a = 255;
    } else if (n == 4) {
        o.r = 255; o.g = 255; o.b = 0; o.a = 255;
    } else if (n == 5) {
        o.r = 255; o.g = 0; o.b = 255; o.a = 255;
    } else if (n == 6) {
        o.r = 0; o.g = 255; o.b = 255; o.a = 255;
    } else if (n == 7) {
        o.r = 255; o.g = 255; o.b = 255; o.a = 255;
    } else if (n == 8) {
        o.r = 127; o.g = 127; o.b = 127; o.a = 255;
    } else if (n == 9) {
        o.r = 64; o.g = 192; o.b = 64; o.a = 255;
    }
    return o;
}

static inline void apply_direction(uint32_t* head_x, uint32_t* head_y, uint32_t* head_dir, char dir) {
    if (*head_dir == 'N' && dir == 'N') {
        *head_y = wrap_y(*head_y - 1);
        *head_dir = 'N';
    } else if (*head_dir == 'N' && dir == 'E') {
        *head_x = wrap_x(*head_x + 1);
        *head_dir = 'E';
    } else if (*head_dir == 'N' && dir == 'W') {
        *head_x = wrap_x(*head_x - 1);
        *head_dir = 'W';
    } else if (*head_dir == 'N' && dir == 'S') {
        *head_y = wrap_y(*head_y + 1);
        *head_dir = 'S';
    } else if (*head_dir == 'S' && dir == 'N') {
       *head_y = wrap_y(*head_y + 1);
       *head_dir = 'S';
    } else if (*head_dir == 'S' && dir == 'E') {
       *head_x = wrap_x(*head_x - 1);
       *head_dir = 'W';
    } else if (*head_dir == 'S' && dir == 'W') {
       *head_x = wrap_x(*head_x + 1);
       *head_dir = 'E';
    } else if (*head_dir == 'S' && dir == 'S') {
       *head_y = wrap_y(*head_y - 1);
       *head_dir = 'N';
    } else if (*head_dir == 'E' && dir == 'N') {
       *head_x = wrap_x(*head_x + 1);
       *head_dir = 'E';
   } else if (*head_dir == 'E' && dir == 'E') {
       *head_y = wrap_y(*head_y + 1);
       *head_dir = 'S';
   } else if (*head_dir == 'E' && dir == 'W') {
       *head_y = wrap_y(*head_y - 1);
       *head_dir = 'N';
   } else if (*head_dir == 'E' && dir == 'S') {
       *head_x = wrap_x(*head_x - 1);
       *head_dir = 'W';
   } else if (*head_dir == 'W' && dir == 'N') {
      *head_x = wrap_x(*head_x - 1);
      *head_dir = 'W';
   } else if (*head_dir == 'W' && dir == 'E') {
      *head_y = wrap_y(*head_y - 1);
      *head_dir = 'N';
   } else if (*head_dir == 'W' && dir == 'W') {
      *head_y = wrap_y(*head_y + 1);
      *head_dir = 'S';
   } else if (*head_dir == 'W' && dir == 'S') {
      *head_x = wrap_x(*head_x + 1);
      *head_dir = 'E';
   }
}

static inline char convert_dir(uint8_t dir) {
    if (dir == '1') {
        return 'N';
    } else if (dir == '2') {
        return 'E';
    } else if (dir == '4') {
        return 'S';
    } else if (dir == '8') {
        return 'W';
    }
    return 'N';
}

void turmite(uchar4* out, uint32_t x, uint32_t y) {
    // Get turmite state from allocation
    uint32_t head_x = rsGetElementAt_uint(head, 0);
    uint32_t head_y = rsGetElementAt_uint(head, 1);
    uint32_t head_state = rsGetElementAt_uint(head, 2);
    uint32_t head_dir = rsGetElementAt_uint(head, 3);

    // Get current pixel color
    uint8_t color = get_color_id(rsGetElementAt_uchar4(in, x, y));

    if (head_x == x && head_y == y) {
        // Get the state number
        uint8_t state_num = head_state * turmite_num_colors + color;

        // Apply the values from turmite definition
        color = turmite_def[(state_num * 3) + 0] - '0';
        apply_direction(&head_x, &head_y, &head_dir, convert_dir(turmite_def[(state_num * 3) + 1]));
        head_state = turmite_def[(state_num * 3) + 2] - '0';

        // Write output state to allocation
        rsSetElementAt_uint(head, head_x, 0);
        rsSetElementAt_uint(head, head_y, 1);
        rsSetElementAt_uint(head, head_state, 2);
        rsSetElementAt_uint(head, head_dir, 3);
    }

    // Write output color
    *out = get_color(color);

}
