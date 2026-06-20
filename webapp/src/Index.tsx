/*global document*/

import {createRoot} from 'react-dom/client';
import App from './App';
import './index.css';
import './i18n';
import axios from 'axios';

// Don't render params with "[]"
axios.defaults.paramsSerializer = {indexes: null};

function showPage() {
    const container = document.getElementById('app');
    const root = createRoot(container);
    root.render(<App/>);
}

document.addEventListener('DOMContentLoaded', showPage);
