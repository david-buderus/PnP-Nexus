export interface SettingsProps<S> {
    settings: S,
    setSettings: (s: S) => void;
    errors: Map<string, string>;
}
