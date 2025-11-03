import {FormErrors} from "@mantine/form";
import axios, {AxiosError} from "axios";
import {ReactNode} from "react";

/** Handles network errors */
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
export function handleValidationErrors(setError: (errors: FormErrors) => void): (err: Error | AxiosError) => void {
    return err => {
        if (!axios.isAxiosError(err)) {
            return;
        }
        if (err.response.status !== 400) {
            handleNetworkErrors()(err);
            return;
        }
        const response = err.response.data;
        const errors: Record<string, ReactNode> = {};

        for (const [key, value] of Object.entries(response.errors)) {
            if (typeof (value) === 'string') {
                errors[key] = value;
            } else {
                console.error("Unknown type during validation " + typeof (value));
            }
        }

        setError(errors);
    };
}

/** Adjusts the form errors to handle the error output of database insertion */
export function handleDatabaseInsertErrors(setError: (errors: FormErrors) => void): (errors: FormErrors) => void {
    return errors => {
        const result: Record<string, ReactNode> = {};

        for (const error in errors) {
            if (!error.startsWith("objects.0.")) {
                console.error("Unexpected error: " + error);
                result[error] = errors[error];
            } else {
                result[error.substring(10)] = errors[error];
            }
        }
        setError(result);
    };
}