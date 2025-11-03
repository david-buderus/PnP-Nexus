import { Select, SelectProps } from "@mantine/core";
import { useTranslation } from "react-i18next";

/** Select over languages */
export default function LanguageSelect(props: Omit<SelectProps, "label" | "data">) {
    const { t } = useTranslation();

    return <Select
        data-testid="language"
        label={t("language")}
        data={[
            {
                value: "de",
                label: "Deutsch"
            },
            {
                value: "en",
                label: "English"
            }
        ]}
        {...props}
    />;
}