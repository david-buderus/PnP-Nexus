import {defineConfig} from 'orval';


export default defineConfig({
    backend: {
        output: {
            mode: 'tags-split',
            target: 'src/api/backend.ts',
            schemas: 'src/api/model',
            client: 'react-query',
            fileExtension: '.ts',
        },
        input: {
            target: './../docs/swagger.json',
        },
    },
});