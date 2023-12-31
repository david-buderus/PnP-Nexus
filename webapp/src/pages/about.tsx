import { FormControl, InputLabel, MenuItem, Select, SelectChangeEvent } from '@mui/material';
import React from 'react';
import i18n from '../i18n';

function About() {
    const [language, setLanguage] = React.useState(i18n.language);

    const handleChange = (event: SelectChangeEvent) => {
        setLanguage(event.target.value);
        i18n.changeLanguage(event.target.value);
    };

    return <div>
        <FormControl>
            <InputLabel>Language</InputLabel>
            <Select
                value={language}
                label="Language"
                onChange={handleChange}
            >
                <MenuItem value={"de"}>Deutsch</MenuItem>
                <MenuItem value={"en"}>English</MenuItem>
            </Select>
        </FormControl>
    </div>
}
export default About;