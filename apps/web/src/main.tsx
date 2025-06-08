import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {ThemeProvider} from "@/components/theme-provider.tsx";
import Layout from './components/layout/layout.tsx';

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
    <StrictMode>
      <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
        <Layout>
          <QueryClientProvider client={queryClient}>
            <App/>
          </QueryClientProvider>
        </Layout>
      </ThemeProvider>
    </StrictMode>,
)
