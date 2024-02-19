import axios, { AxiosError } from "axios";


/** Handles api errors and returns the resulting error map. */
export function handleValidationError(setError: (errors: Map<string, string>) => void, keyFormatter?: (key: string) => string): (err: Error | AxiosError) => void {
    return err => {
        if (!axios.isAxiosError(err)) {
            return;
        }
        if (err.response.status !== 400) {
            return;
        }
        const errorMap = new Map<string, string>();
        Object.entries(err.response.data).forEach(entry => {
            let [key, value] = entry;

            if (keyFormatter) {
                key = keyFormatter(key);
            }

            errorMap[key] = value;
        });
        setError(errorMap);
    };
}