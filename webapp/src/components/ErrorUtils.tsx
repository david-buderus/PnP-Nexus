import axios, { AxiosError } from "axios";

export function handleNetworkErrors(): (err: Error | AxiosError) => void {
    return err => {
        if (!axios.isAxiosError(err)) {
            return;
        }
        if (err.response.status === 401 || err.response.status === 405) {
            location.reload();
        }
    };
}


/** Handles api errors and returns the resulting error map. */
export function handleValidationErrors(setError: (errors: Map<string, string>) => void, keyFormatter?: (key: string) => string): (err: Error | AxiosError) => void {
    return err => {
        if (!axios.isAxiosError(err)) {
            return;
        }
        if (err.response.status !== 400) {
            handleNetworkErrors()(err);
            return;
        }
        const errorMap = new Map<string, string>();
        Object.entries(err.response.data).forEach(entry => {
            let [key, value] = entry;

            if (keyFormatter) {
                key = keyFormatter(key);
            }

            errorMap.set(key, value as string);
        });
        setError(errorMap);
    };
}