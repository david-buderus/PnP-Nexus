import { Button, DialogActions, DialogTitle, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, Stack } from '@mui/material';
import React, { useState } from 'react';
import i18n from '../i18n';
import { CloseReasons, openDialog } from '../components/DialogUtils';

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
        <Button onClick={() => {
            openDialog(close => <DialogContent close={close} />);
        }}>
            MY BUTTON
        </Button>
    </div>;
}
export default About;


function DialogContent({
    close
}: {
    close: (event: unknown, reason: CloseReasons) => void;
}) {
    const [content, setContent] = useState(10);

    return <>
        <DialogTitle>Some Title</DialogTitle>
        <Stack spacing={2} padding={2}>
            <Stack>
                {content}
                <Button data-testid="dialog-cancel" autoFocus onClick={() => setContent(content + 1)}>
                    Give it
                </Button>
            </Stack>
        </Stack>
        <DialogActions>
            <Button data-testid="dialog-cancel" autoFocus onClick={() => close(null, "cancel")}>
                Cancel
            </Button>
            <Button data-testid="dialog-action" variant="contained" color="success" onClick={() => close(null, "successful")
            }>
                Save
            </Button>
        </DialogActions>
    </>;
}