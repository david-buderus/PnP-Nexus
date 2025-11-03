/**
 * Returns the probability for a throw with at least the given values.
 */
export function probabilityForSuccesfulThrows(a1: number, a2: number, a3: number, succesfulThrows: number = 2) {
    const p1 = probabilities(a1);
    const p2 = probabilities(a2);
    const p3 = probabilities(a3);

    let chance = 0;
    for (let x1 = -1; x1 < 3; x1++) {
        for (let x2 = -1; x2 < 3; x2++) {
            for (let x3 = -1; x3 < 3; x3++) {
                if (x1 + x2 + x3 >= succesfulThrows) {
                    chance += p1[x1] * p2[x2] * p3[x3];
                }
            }
        }
    }
    return chance;
}

function probabilities(x: number): {
    "-1": number;
    "0": number;
    "1": number;
    "2": number;
} {
    if (x < 1) {
        return {
            "-1": 1 / 20,
            "0": 18 / 20,
            "1": 1 / 20,
            "2": 0
        };
    }
    if (x >= 20) {
        return {
            "-1": 1 / 20,
            "0": 0,
            "1": 18 / 20,
            "2": 1 / 20
        };
    }
    return {
        "-1": 1 / 20,
        "0": (19 - x) / 20,
        "1": (x - 1) / 20,
        "2": 1 / 20
    };
}
