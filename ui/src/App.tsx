import "@salesforce-ux/design-system/assets/styles/salesforce-lightning-design-system.css"
import "./App.css";
import {useState} from 'react';
import { Input, Button, Card, Spinner, ExpandableSection } from '@salesforce/design-system-react';
import ky from 'ky';
import { marked } from 'marked';

const tenantId = '123';

function App() {
  const [query, setQuery] = useState<string>('');
  const [file, setFile] = useState<File | null>(null);
  const [conversation, setConversation] = useState<ConversationMessage[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isUploadOpen, setIsUploadOpen] = useState<boolean>(false);
  
  const api = ky.extend({
    hooks: {
      beforeRequest:[(_: any)=> setIsLoading(true)],
      afterResponse:[(_: any)=> setIsLoading(false)],
    },
    timeout: false,
  });
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setQuery(event.currentTarget.value);
  }

  const handleUpload = (_: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    let formData = new FormData();
    formData.append('file', file);
    formData.append('tenantId', tenantId);
    api.post('/api/documents', {
      body: formData
    })
  }

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (!event.currentTarget.files) {
      return;
    }
    setFile(event.currentTarget.files[0]);
  }

  const handleSubmit = async (_: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    const response = await api.get(`/api/documents?question=${query}&tenantId=${tenantId}`)
    setConversation([...conversation, {question: query, response: await response.text()}])
  }

  return (
    <div class="container">
      <div class="left">
        {/* <ExpandableSection id="file-upload" title="Upload a File" isOpen={isUploadOpen} onToggleOpen={() => setIsUploadOpen(!isUploadOpen)} classNames="flex-row"> */}
        {/*   <Input type="file" label="File Upload" className="flex-column align-start" onChange={handleFileChange} /> */}
        {/*   <Button label="Upload" className="flex-column align-end" onClick={handleUpload}/> */}
        {/* </ExpandableSection> */}
        <form>
          <Input label="Query" onChange={handleInputChange} onSubmit={handleSubmit}/>
          <Button label="Chat" variant="brand" onClick={handleSubmit} disabled={isLoading} />
        </form>
        <div>
          File List will go here
        </div>
      </div>
      <div class="right">
        {conversation.map((msg, index) => {
            return (
                <Card id={index} heading={msg.question}>
                    <div class="question-body" dangerouslySetInnerHTML={{ __html: marked.parse(msg.response) }}></div>
                </Card>
            );
        })}
      </div>
    {isLoading &&<Spinner size="large" variant="base" isVisible={isLoading} assistiveText={{ label: 'Loading' }} />}
    </div>
  )
}

type ConversationMessage = {
  question: string;
  response: string;
}

export default App

