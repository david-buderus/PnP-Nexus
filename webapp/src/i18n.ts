import i18n from "i18next";
import Backend from 'i18next-http-backend';
import {initReactI18next} from "react-i18next";
import LanguageDetector from 'i18next-browser-languagedetector';

i18n.use(Backend).use(LanguageDetector).use(initReactI18next).init({
    fallbackLng: "en",
    ns: ['translation', 'enum', 'item', 'error', 'universe', 'permission', 'user', 'crafting', 'upgrade', 'spell', 'character', 'admin', 'species', 'sheetEditor'],
    backend: {
        loadPath: '/locales/{{lng}}/{{ns}}.json'
    }
});

export default i18n;
