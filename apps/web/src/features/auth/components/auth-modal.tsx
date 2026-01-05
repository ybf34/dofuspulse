import {useNavigate} from "@tanstack/react-router";
import {useState} from "react";
import {Dialog, DialogContent} from "@/components/ui/dialog";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs";
import {useLoginMutation} from "@/features/auth/hooks/use-login-mutation";
import {useRegisterMutation} from "@/features/auth/hooks/use-register-mutation";
import {getOAuth2AuthorizationUrl} from "@/features/auth/utils/auth";
import {LoginForm} from "./login-form";
import {RegisterForm} from "./register-form";
import {OAuthButtons} from "./oauth-buttons";
import type {OAuth2Provider} from "@/types/types";

interface AuthModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  defaultTab?: "login" | "signup";
}

export function AuthModal({
                            open,
                            onOpenChange,
                            defaultTab = "login",
                          }: AuthModalProps) {
  const navigate = useNavigate();

  const [activeTab, setActiveTab] = useState(defaultTab);
  const {login, isPending: isLoginPending} = useLoginMutation();
  const {register, isPending: isRegisterPending} = useRegisterMutation();

  const handleLogin = async (data: { email: string; password: string }) => {
    await login({body: data});
    await navigate({to: "/gearsets"});
    onOpenChange(false);
  };

  const handleSignup = async (data: { email: string; password: string }) => {
    await register({body: data});
    await navigate({to: "/gearsets"});
    onOpenChange(false);
  };

  const initiateOAuth2Login = (provider: OAuth2Provider) => {
    window.location.href = getOAuth2AuthorizationUrl(provider);
  };

  return (
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="sm:max-w-md bg-black border-zinc-800 text-white">
          <Tabs
              value={activeTab}
              onValueChange={(v) => setActiveTab(v as "login" | "signup")}
              className="w-full mt-5"
          >
            <TabsList
                className="grid w-full grid-cols-2 bg-zinc-900/50 border border-zinc-800 p-1 rounded-full">
              <TabsTrigger
                  value="login"
                  className="data-[state=active]:bg-white data-[state=active]:text-black data-[state=inactive]:text-zinc-400 rounded-full font-medium transition-all"
              >
                Login
              </TabsTrigger>
              <TabsTrigger
                  value="signup"
                  className="data-[state=active]:bg-white data-[state=active]:text-black data-[state=inactive]:text-zinc-400 rounded-full font-medium transition-all"
              >
                Sign up
              </TabsTrigger>
            </TabsList>

            <TabsContent value="login" className="mt-6">
              <div className="grid gap-4">
                <OAuthButtons onOAuthLogin={initiateOAuth2Login}/>

                <div
                    className="after:border-zinc-800 relative text-center text-sm after:absolute after:inset-0 after:top-1/2 after:z-0 after:flex after:items-center after:border-t">
								<span className="bg-black text-zinc-400 relative z-10 px-2">
									Or use email
								</span>
                </div>
              </div>

              <LoginForm onSubmit={handleLogin} isPending={isLoginPending}/>
            </TabsContent>

            <TabsContent value="signup" className="mt-6">
              <div className="grid gap-4">
                <OAuthButtons onOAuthLogin={initiateOAuth2Login}/>

                <div
                    className="after:border-zinc-800 relative text-center text-sm after:absolute after:inset-0 after:top-1/2 after:z-0 after:flex after:items-center after:border-t">
								<span className="bg-black text-zinc-400 relative z-10 px-2">
									Or create an account with email
								</span>
                </div>
              </div>

              <RegisterForm
                  onSubmit={handleSignup}
                  isPending={isRegisterPending}
              />
            </TabsContent>
          </Tabs>
        </DialogContent>
      </Dialog>
  );
}
